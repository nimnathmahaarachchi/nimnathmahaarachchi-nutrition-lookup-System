package nutrition

case class Fruit(
  override val name: String,
  override val calories: Double,
  override val protein: Double,
  override val carbohydrates: Double,
  override val fat: Double,
  val vitaminC: Double,
  val fiber: Double
) extends Food(name, calories, protein, carbohydrates, fat) with NutrientRich {

  override def getCategory: String = "Fruit"
  
  override def getNutritionSummary: String = {
    super.getNutritionSummary + f", ${vitaminC}%.1f mg Vitamin C, ${fiber}%.1f g fiber"
  }
  
  override def isHealthy: Boolean = super.isHealthy && vitaminC > 10
  
  override def getKeyNutrients: List[String] = List("Vitamin C", "Fiber", "Natural Sugars")
}