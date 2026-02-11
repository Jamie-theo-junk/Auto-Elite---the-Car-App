import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.harmless.autoelitekotlin.model.CarBrand
import com.harmless.autoelitekotlin.model.CarModel



object SelectedValues {

     private const val PREF_NAME = "selected_values_prefs"
     private lateinit var appContext: Context
     private val gson = Gson()

     fun init(context: Context) {
          appContext = context.applicationContext
          loadAll()
     }

     private val prefs by lazy {
          appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
     }

     private fun save(key: String, value: Any?) {
          val editor = prefs.edit()
          when (value) {
               is String -> editor.putString(key, value)
               is Int -> editor.putInt(key, value)
               is Boolean -> editor.putBoolean(key, value)
               is Float -> editor.putFloat(key, value)
               is Long -> editor.putLong(key, value)
               is List<*> -> editor.putString(key, gson.toJson(value))
               is MutableList<*> -> editor.putString(key, gson.toJson(value))
               null -> editor.remove(key)
               else -> editor.putString(key, gson.toJson(value))
          }
          editor.apply()
     }

     private fun <T> load(key: String, clazz: Class<T>): T? {
          val json = prefs.getString(key, null) ?: return null
          return try { gson.fromJson(json, clazz) } catch (_: Exception) { null }
     }

     private fun <T> loadList(key: String, clazz: Class<Array<T>>): MutableList<T> {
          val json = prefs.getString(key, null) ?: return mutableListOf()
          return try { gson.fromJson(json, clazz).toMutableList() } catch (_: Exception) { mutableListOf() }
     }

     // -------------------------------------------------
     // 🔹 NEWLY UPDATED + ADDED PROPERTIES
     // -------------------------------------------------

     // Brand names only (strings)
     var carBrandsSelected: MutableList<String>
          get() = loadList("carBrandsSelected", Array<String>::class.java)
          set(value) = save("carBrandsSelected", value)

     // Model selections: brand → model
     var carModelsSelected: MutableList<Pair<String, String>>
          get() {
               val json = prefs.getString("carModelsSelected", null) ?: return mutableListOf()
               return try {
                    val type = object : TypeToken<MutableList<Pair<String, String>>>() {}.type
                    gson.fromJson(json, type)
               } catch (_: Exception) {
                    mutableListOf()
               }
          }
          set(value) = save("carModelsSelected", value)

     // Variant selections: brand → model → variant
     var carVariantsSelected: MutableList<Triple<String, String, String>>
          get() {
               val json = prefs.getString("carVariantsSelected", null) ?: return mutableListOf()
               return try {
                    val type = object : TypeToken<MutableList<Triple<String, String, String>>>() {}.type
                    gson.fromJson(json, type)
               } catch (_: Exception) {
                    mutableListOf()
               }
          }
          set(value) = save("carVariantsSelected", value)

     // -------------------------------------------------
     // 🔹 Existing Filters
     // -------------------------------------------------

     var selectedType: String?
          get() = prefs.getString("selectedType", null)
          set(value) = save("selectedType", value)

     var selectedTransmission: String?
          get() = prefs.getString("selectedTransmission", null)
          set(value) = save("selectedTransmission", value)

     var selectedYear: MutableList<Int>
          get() = loadList("selectedYear", Array<Int>::class.java)
          set(value) = save("selectedYear", value)

     var selectedMinPrice: String?
          get() = prefs.getString("selectedMinPrice", null)
          set(value) = save("selectedMinPrice", value)

     var selectedMaxPrice: String?
          get() = prefs.getString("selectedMaxPrice", null)
          set(value) = save("selectedMaxPrice", value)

     var selectedColor: MutableList<String>
          get() = loadList("selectedColor", Array<String>::class.java)
          set(value) = save("selectedColor", value)

     var selectedProvince: MutableList<String>
          get() = loadList("selectedProvince", Array<String>::class.java)
          set(value) = save("selectedProvince", value)

     var selectedFuelType: String?
          get() = prefs.getString("selectedFuelType", null)
          set(value) = save("selectedFuelType", value)

     var isNewOrUsed: String?
          get() = prefs.getString("isNewOrUsed", null)
          set(value) = save("isNewOrUsed", value)

     var selectedMaxMileage: Int?
          get() = if (prefs.contains("selectedMaxMileage")) prefs.getInt("selectedMaxMileage", 0) else null
          set(value) = save("selectedMaxMileage", value ?: 0)

     var selectedMinMileage: Int?
          get() = if (prefs.contains("selectedMinMileage")) prefs.getInt("selectedMinMileage", 0) else null
          set(value) = save("selectedMinMileage", value ?: 0)

     var selectedMileage: String?
          get() = prefs.getString("selectedMileage", null)
          set(value) = save("selectedMileage", value)

     var selectedDriveTrain: String?
          get() = prefs.getString("selectedDriveTrain", null)
          set(value) = save("selectedDriveTrain", value)


     fun clear() {
          prefs.edit().clear().apply()
     }

     private fun loadAll() {
          carBrandsSelected
          carModelsSelected
          carVariantsSelected
          selectedYear
          selectedColor
          selectedProvince
     }
}
