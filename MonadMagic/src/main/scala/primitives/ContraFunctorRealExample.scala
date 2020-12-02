package primitives

object ContraFunctorRealExample extends App {

  trait PaymentOrderSubmitter[A] {
    def submit(value: A): Boolean
  }

  def submit[A](value: A)(implicit p: PaymentOrderSubmitter[A]): Boolean = p.submit(value)

}