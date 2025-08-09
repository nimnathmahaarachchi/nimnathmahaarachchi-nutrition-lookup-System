package nutrition

case class Grain(
  override val name: String,
  override val calories: Double,
  override val protein: Double,
  override val carbohydrates: Double,
  override val fat: Double,
  val fiber: Double,
  val isWhole: Boolean
) extends Food(name, calories, protein, carbohydrates, fat) with NutrientRich {

  override def getCategory: String = if (isWhole) "Whole Grain" else "Refined Grain"
  
  override def getNutritionSummary: String = {
    val grainType = if (isWhole) "whole grain" else "refined grain"
    super.getNutritionSummary + f", ${fiber}%.1f g fiber ($grainType)"
  }
  
  override def isHealthy: Boolean = super.isHealthy && isWhole && fiber > 2
  
  override def getKeyNutrients: List[String] = 
    if (isWhole) List("Fiber", "B Vitamins", "Complex Carbohydrates")
    else List("Simple Carbohydrates", "Energy")
}