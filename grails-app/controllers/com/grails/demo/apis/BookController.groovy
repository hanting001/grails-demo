package com.grails.demo.apis



import static org.springframework.http.HttpStatus.*
import grails.converters.JSON
import grails.transaction.Transactional

import org.bson.types.ObjectId

@Transactional(readOnly = true)
class BookController  {

	static allowedMethods = [save: "POST", update: "PUT",  delete: "DELETE"]

	/*
	 * 对应GET请求
	 * http://localhost:8080/apis/book?format=json
	 * format=json表示该请求的返回类型希望是json格式
	 */
	def index(Integer max) {
		params.max = Math.min(max ?: 10, 100)
		params.putAll([sort:'title',order:'asc'])

		def result = [total:Book.count(), books:Book.list(params)]
		respond result
	}

	def show(Book bookInstance) {
		respond bookInstance
	}


	/*
	 * 对应POST请求
	 * http://localhost:8080/apis/book?format=json
	 * http header：Content-Type = application/json
	 * 提交json数据为
	 * {
	 *	"title":"Node.js实战",
	 *	"descript":"node.js in action中文版",
	 *	"price":23,
	 *	"shopURL":"http://product.china-pub.com/3769925"
	 *}
	 */
	@Transactional
	def save(Book bookInstance) {
		if (bookInstance == null) {
			notFound()
			return
		}

		if (bookInstance.hasErrors()) {
			respond bookInstance.errors
			return
		}
		if (Book.findByTitle(bookInstance.title)) {
			bookInstance = Book.findByTitle(bookInstance.title)
		} else {
			bookInstance.id= new ObjectId()
			bookInstance.save flush:true
		}
		
		respond bookInstance, [status: OK]
		
	}


	/*
	 * 对应PUT请求
	 * http://localhost:8080/apis/book/5387d59f9475774da496ce09?format=json
	 * 提交json数据为
	 * {
	 *       "price": 50.37 //需要修改的属性
	 * }
	 */
	@Transactional
	def update(Book bookInstance) {
		if (bookInstance == null) {
			notFound()
			return
		}

		if (bookInstance.hasErrors()) {
			respond bookInstance.errors
			return
		}

		bookInstance.save flush:true

		respond bookInstance
	}

	/*
	 * 对应DELETE请求
	 * http://localhost:8080/apis/book/5387da099475774da496ce0b/?format=json
	 */
	@Transactional
	def delete(Book bookInstance) {

		if (bookInstance == null) {
			notFound()
			return
		}
		bookInstance.delete()
		render "sucess"
	}

	/*
	 * 对应POST请求
	 * http://localhost:8080/apis/book/findByTitle?format=json
	 * 使用request.JSON获取提交的json数据
	 * 提交json数据为
	 * {
	 * 	"title":"Node"
	 * }
	 */
	def findByTitle() {
		def books = Book.findAllByTitleLike((request.JSON.title?:'') + '%', [sort:'title',order:'desc',max:100] );
		respond books;
	}
	
	
	protected void notFound() {
		render status: NOT_FOUND
	}
}
