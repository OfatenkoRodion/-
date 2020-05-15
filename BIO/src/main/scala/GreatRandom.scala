object GreatRandom {

  private val rnd = new scala.util.Random

  val hpDefault = 100

  def rndAnimalA =  AnimalA(rnd.nextInt(750)+25, rnd.nextInt(750)+25, hpDefault)

  def rndAnimalB =  AnimalB(rnd.nextInt(750)+25, rnd.nextInt(750)+25, hpDefault)

  def rndAnimalC =  AnimalC(rnd.nextInt(750)+25, rnd.nextInt(750)+25, 200)

  def rndResource = Resource(rnd.nextInt(750)+25, rnd.nextInt(750)+25)

  def rndAnimalAWithFixStartPosition(x:Int, y: Int) =  AnimalA(x, y, hpDefault)

  def rndAnimalBWithFixStartPosition(x:Int, y: Int) =  AnimalB(x, y, hpDefault)

  def rndAnimalCWithFixStartPosition(x:Int, y: Int) =  AnimalC(x, y, hpDefault)

  def rndScenario1: List[Animal] =
    List(
      rndAnimalA,
      rndAnimalA,
      rndAnimalA,
      rndAnimalB,
      rndAnimalB,
      rndAnimalB,
      rndAnimalC,
      rndAnimalC,
      rndAnimalC
  )

  def rndScenario2: List[Resource] =
    List(
      rndResource,
      rndResource,
      rndResource,
      rndResource,
      rndResource,
      rndResource,
      rndResource,
      rndResource,
      rndResource
    )
}
