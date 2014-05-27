package com.grails.demo.apis

import org.bson.types.ObjectId
import org.grails.databinding.BindUsing

class Book {
	@BindUsing({
		obj, source ->
		//第一个参数是需要绑定的对象，第二个参数是用于绑定的数据源
		source['id']?new ObjectId(source['id']):new ObjectId()
	})
	ObjectId id
	
	String title

	String descript
	double price
	String shopURL

	static transactional = 'mongo'
	static constraints = {
	}
	static mapping = {
		id generator: 'assigned'
		title index:true
	}
}
