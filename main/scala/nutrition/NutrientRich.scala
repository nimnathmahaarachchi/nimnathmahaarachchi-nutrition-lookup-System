package nutrition

trait NutrientRich {
  def getKeyNutrients: List[String]
  
  def hasNutrient(nutrient: String): Boolean = {
    getKeyNutrients.exists(_.toLowerCase.contains(nutrient.toLowerCase))
  }
}