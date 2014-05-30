package com.grails.demo.apis



import static org.springframework.http.HttpStatus.*
import grails.converters.JSON
import grails.transaction.Transactional

import org.bson.types.ObjectId

@Transactional(readOnly = true)
class BookController  {

	static allowedMethods = [save: "POST", update: "PUT",  delete: "DELETE"]

	/*
	 * ��ӦGET����
	 * http://localhost:8080/apis/book?format=json
	 * format=json��ʾ������ķ�������ϣ����json��ʽ
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
	 * ��ӦPOST����
	 * http://localhost:8080/apis/book?format=json
	 * http header��Content-Type = application/json
	 * �ύjson����Ϊ
	 * {
	 *	"title":"Node.jsʵս",
	 *	"descript":"node.js in action���İ�",
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
	 * ��ӦPUT����
	 * http://localhost:8080/apis/book/5387d59f9475774da496ce09?format=json
	 * �ύjson����Ϊ
	 * {
	 *       "price": 50.37 //��Ҫ�޸ĵ�����
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
	 * ��ӦDELETE����
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
	 * ��ӦPOST����
	 * http://localhost:8080/apis/book/findByTitle?format=json
	 * ʹ��request.JSON��ȡ�ύ��json����
	 * �ύjson����Ϊ
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
