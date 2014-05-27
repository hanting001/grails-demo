import org.bson.types.ObjectId
import grails.converters.JSON
class BootStrap {

    def init = { servletContext ->
		JSON.registerObjectMarshaller(ObjectId) {
			return it?.toString()
		}
    }
    def destroy = {
    }
}
