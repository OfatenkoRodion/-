package primitives

object ContraFunctorRealExample extends App {

  case class InternalPaymentOrder(paymentIds: Seq[Int])
  case class ExternalPaymentOrder(paymentIds: Seq[Int])

  trait PaymentOrderSubmitter[A] {
    def submit(value: A): Boolean
  }

  def submit[A](value: A)(implicit p: PaymentOrderSubmitter[A]): Boolean = p.submit(value)

  implicit val InternalPaymentOrderSubmitter =
    new PaymentOrderSubmitter[InternalPaymentOrder] {
      override def submit(value: InternalPaymentOrder): Boolean = value.paymentIds.length > 10
    }

  implicit val ExternalPaymentOrderSubmitter =
    new PaymentOrderSubmitter[ExternalPaymentOrder] {
      override def submit(value: ExternalPaymentOrder): Boolean = value.paymentIds.length < 10
    }

  val submitRes1 = submit(InternalPaymentOrder(1 to 10))
  val submitRes2 = submit(ExternalPaymentOrder(1 to 10))

  println(submitRes1)
  println(submitRes2)

}