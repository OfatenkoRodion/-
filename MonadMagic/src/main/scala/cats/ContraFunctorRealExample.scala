package cats

object ContraFunctorRealExample extends App {

  case class InternalPaymentOrder(paymentIds: Seq[Int])
  case class ExternalPaymentOrder(paymentIds: Seq[Int])
  case class SelfEmployedPaymentOrder(paymentIds: Seq[Int])

  trait PaymentOrderSubmitter[A] {
    self: PaymentOrderSubmitter[A] =>

    def submit(value: A): Boolean

    def contramap[B](convert: B => A, func: B => Boolean) : PaymentOrderSubmitter[B] =
      new PaymentOrderSubmitter[B] {
        override def submit(value: B): Boolean =
          self.submit(convert(value)) && func(value)
      }
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

 /* implicit val SelfEmployedPaymentOrderSubmitter =
    new PaymentOrderSubmitter[SelfEmployedPaymentOrder] {
      override def submit(value: SelfEmployedPaymentOrder): Boolean = value.paymentIds.length > 10 && value.paymentIds.head != 1
    }*/

  implicit def SelfEmployedPaymentOrderSubmitter(implicit submitter: PaymentOrderSubmitter[InternalPaymentOrder]): PaymentOrderSubmitter[SelfEmployedPaymentOrder] =
    submitter.contramap(v => InternalPaymentOrder(v.paymentIds), _.paymentIds != 1)

  val submitRes1 = submit(InternalPaymentOrder(1 to 10))
  val submitRes2 = submit(ExternalPaymentOrder(1 to 10))
  val submitRes3 = submit(SelfEmployedPaymentOrder(1 to 10))

  println(submitRes1)
  println(submitRes2)
  println(submitRes3)

}
