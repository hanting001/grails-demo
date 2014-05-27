package com.grails.demo.apis



import static org.springframework.http.HttpStatus.*
import grails.converters.JSON
import grails.transaction.Transactional

import org.bson.types.ObjectId

@Transactional(readOnly = true)
class BookController  {

	static allowedMethods = [save: "POST", update: "PUT",  delete: "DELETE"]

	def index(Integer max) {
		params.max = Math.min(max ?: 10, 100)
		params.putAll([sort:'title',order:'asc'])

		def result = [total:Book.count(), books:Book.list(params)]
		respond result
	}

	def show(Book bookInstance) {
		respond bookInstance
	}



	@Transactional
	def save(Book bookInstance) {
		if (bookInstance == null) {
			notFound()
			return
		}

		if (bookInstance.hasErrors()) {
			respond bookInstance.errors, view:'create'
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



	@Transactional
	def update(Book bookInstance) {
		if (bookInstance == null) {
			notFound()
			return
		}

		if (bookInstance.hasErrors()) {
			respond bookInstance.errors, view:'edit'
			return
		}

		bookInstance.save flush:true

		respond bookInstance
	}

	@Transactional
	def delete(Book bookInstance) {

		if (bookInstance == null) {
			notFound()
			return
		}

		render "sucess"
	}

	
	def findByTitle(Book bookInstance) {
		
		def books = Book.findAllByTitleLike((bookInstance.title?:'') + '%', [sort:'title',order:'desc',max:100] );
		respond books;
	}
	
	
	protected void notFound() {
		render status: NOT_FOUND
	}
}
