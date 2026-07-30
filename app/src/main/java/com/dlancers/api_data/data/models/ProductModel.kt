import com.google.gson.annotations.SerializedName
data class Product (
    val id :Int?,
    @SerializedName("title")
    val name :String?,
    val price : Double? ,
    val description :String?,
    val images : List<String>?

)