import kotlin.test.Test
import kotlin.test.assertEquals

class PlatformTest {
   @Test
   fun test() {
      println("Hello from commonTest")
      assertEquals(2, 1, "1 should be 1")
   }


   fun beforeAll() {
      println("Before all tests")
   }
}