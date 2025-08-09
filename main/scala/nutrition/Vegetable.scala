package nutrition

case class Vegetable(
  override val name: String,
  override val calories: Double,
  override val protein: Double,
  override val carbohydrates: Double,
  override val fat: Double,
  val vitaminA: Double,
  val iron: Double
) extends Food(name, calories, protein, carbohydrates, fat) with NutrientRich {

  override def getCategory: String = "Vegetable"
  
  override def getNutritionSummary: String = {
    super.getNutritionSummary + f", ${vitaminA}%.1f mcg Vitamin A, ${iron}%.1f mg Iron"
  }
  
  override def isHealthy: Boolean = super.isHealthy && iron > 0.5
  
  override def getKeyNutrients: List[String] = List("Vitamin A", "Iron", "Antioxidants")
}