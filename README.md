grails-demo
===========
一个简单的采用grails开发的rest apis应用。数据库采用mongodb，使用grails mongodb plugin。

##demain
```groovy
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

...
