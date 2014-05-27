grails-demo
===========
一个简单的采用grails开发的rest apis应用。数据库采用mongodb，使用grails mongodb plugin。

##demain
一个简单的book对象
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
##controller
book controller提供rest服务。
在UrlMapping.groovy中定义
...groovy
"/book"(resources:'book')
...
rest资源对应的url为：
<table class="wiki-table" style="color: #444444; font-size: 14px; font-family: Arial;" border="0" cellspacing="0" cellpadding="0">
<tbody>
<tr><th style="color: inherit; font-size: 1em;">HTTP Method</th><th style="color: inherit; font-size: 1em;">URI</th><th style="color: inherit; font-size: 1em;">Controller Action</th></tr>
<tr class="table-odd" style="color: inherit; background-color: #f8f8f8;">
<td>GET</td>
<td>/books</td>
<td>index</td>
</tr>
<tr class="table-odd" style="color: inherit; background-color: #f8f8f8;">
<td>POST</td>
<td>/books</td>
<td>save</td>
</tr>
<tr class="table-even">
<td>GET</td>
<td>/books/${id}</td>
<td>show</td>
</tr>
<tr class="table-even">
<td>PUT</td>
<td>/books/${id}</td>
<td>update</td>
</tr>
<tr class="table-odd" style="color: inherit; background-color: #f8f8f8;">
<td>DELETE</td>
<td>/books/${id}</td>
<td>delete</td>
</tr>
</tbody>
</table>
另外又针对http + json请求增加了一个action：findByTitle
...groovy
	def findByTitle(Book bookInstance) {
		
		def books = Book.findAllByTitleLike((bookInstance.title?:'') + '%', [sort:'title',order:'desc',max:100] );
		respond books;
	}
...
