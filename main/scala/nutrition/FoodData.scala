package nutrition

object FoodData {
  val sampleFoods: List[Food] = List(
    // Fruits
    Fruit("Apple", 95, 0.5, 25, 0.3, 8.4, 4.4),
    Fruit("Banana", 105, 1.3, 27, 0.4, 10.3, 3.1),
    Fruit("Orange", 85, 1.2, 21, 0.2, 92.9, 3.0),
    Fruit("Strawberry", 49, 1.0, 12, 0.5, 89.4, 3.3),
    Fruit("Mango", 107, 1.4, 28, 0.6, 67.0, 2.6),
    Fruit("Grapes", 104, 1.1, 27, 0.2, 10.8, 1.4),
    Fruit("Pineapple", 74, 0.9, 20, 0.2, 78.9, 2.3),
    Fruit("Watermelon", 46, 0.9, 12, 0.2, 12.3, 0.6),

    // Vegetables
    Vegetable("Broccoli", 34, 2.8, 7, 0.4, 623, 0.7),
    Vegetable("Carrot", 41, 0.9, 10, 0.2, 1025, 0.3),
    Vegetable("Spinach", 23, 2.9, 4, 0.4, 469, 2.7),
    Vegetable("Bell Pepper", 31, 1.0, 7, 0.3, 157, 0.4),
    Vegetable("Tomato", 18, 0.9, 4, 0.2, 42, 0.3),
    Vegetable("Lettuce", 15, 1.4, 3, 0.2, 174, 1.0),
    Vegetable("Cucumber", 16, 0.7, 4, 0.1, 5, 0.3),
    Vegetable("Sweet Potato", 112, 2.0, 26, 0.1, 961, 0.6),

    // Grains
    Grain("Brown Rice", 216, 5.0, 45, 1.8, 3.5, true),
    Grain("White Rice", 205, 4.3, 45, 0.4, 0.6, false),
    Grain("Quinoa", 222, 8.1, 39, 3.6, 2.8, true),
    Grain("Whole Wheat Bread", 247, 13.2, 41, 4.2, 6.0, true),
    Grain("White Bread", 265, 9.0, 49, 3.2, 2.7, false),
    Grain("Oatmeal", 158, 5.9, 28, 3.2, 4.0, true),
    Grain("Corn", 125, 4.7, 27, 1.9, 7.3, true),
    Grain("Barley", 193, 3.6, 44, 1.2, 6.0, true)
  )

  def getAllFoods: List[Food] = sampleFoods
  
  def getFoodsByCategory(category: String): List[Food] = {
    sampleFoods.filter(_.getCategory.toLowerCase.contains(category.toLowerCase))
  }
  
  def searchFoodsByName(name: String): List[Food] = {
    sampleFoods.filter(_.name.toLowerCase.contains(name.toLowerCase))
  }
}