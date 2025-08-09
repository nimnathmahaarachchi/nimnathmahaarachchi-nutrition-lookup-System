package nutrition

import scala.collection.mutable.ListBuffer

class NutritionDatabase {
  private val foods = ListBuffer[Food](FoodData.getAllFoods: _*)
  
  def searchByName(name: String): List[Food] = {
    if (name.trim.isEmpty) foods.toList
    else foods.filter(_.name.toLowerCase.contains(name.toLowerCase.trim)).toList
  }
  
  def searchByCategory(category: String): List[Food] = {
    if (category.trim.isEmpty || category.toLowerCase == "all") foods.toList
    else foods.filter(_.getCategory.toLowerCase.contains(category.toLowerCase.trim)).toList
  }
  
  def addFood(food: Food): Unit = {
    foods += food
  }
  
  def getAllFoods: List[Food] = foods.toList
  
  def getCategories: List[String] = {
    foods.map(_.getCategory).distinct.toList.sorted
  }
  
  def getHealthyFoods: List[Food] = {
    foods.filter(_.isHealthy).toList
  }
  
  def getFoodsByNutrient(nutrient: String): List[Food] = {
    foods.collect {
      case f: NutrientRich if f.hasNutrient(nutrient) => f.asInstanceOf[Food]
    }.toList
  }
  
  def getFoodCount: Int = foods.size
  
  def getAverageCalories: Double = {
    if (foods.isEmpty) 0.0
    else foods.map(_.calories).sum / foods.size
  }
  
  def getNutritionSummary: String = {
    val totalFoods = getFoodCount
    val avgCalories = getAverageCalories
    val categories = getCategories
    val healthyCount = getHealthyFoods.size
    
    s"""Nutrition Database Summary:
       |Total Foods: $totalFoods
       |Categories: ${categories.mkString(", ")}
       |Average Calories: ${avgCalories.formatted("%.1f")}
       |Healthy Foods: $healthyCount (${(healthyCount.toDouble / totalFoods * 100).formatted("%.1f")}%)
       |""".stripMargin
  }
}