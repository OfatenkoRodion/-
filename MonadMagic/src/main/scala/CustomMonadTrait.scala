object MonadV1 extends App {

  trait Monad[F[_]] {
    def unit[A](a: => A): F[A]

    def flatMap[A, B](ma: F[A])(f: A => F[B]): F[B]
  }

  val optionMonad: Monad[Option] = new Monad[Option] {
    override def unit[A](a: => A): Option[A] = Some(a)

    override def flatMap[A, B](ma: Option[A])(f: A => Option[B]): Option[B] =
      ma match {
        case None => None
        case Some(a) => f(a)
      }
  }

  val maybeName: Option[String] = optionMonad.unit("Fred")
  val maybeSurname: Option[String] = optionMonad.unit("Smith")

  val mayBeFullName =
    optionMonad.flatMap(maybeName) { name =>
      optionMonad.flatMap(maybeSurname) { surname =>
        optionMonad.unit(s"$name $surname")
      }
    }

  println(mayBeFullName)
}

object MonadV2 extends App {

  trait Functor[F[_]] {
    def map[A, B](ma: F[A])(f: A => B): F[B]
  }

  trait Monad[F[_]] extends Functor[F] {
    def unit[A](a: => A): F[A]
    def flatMap[A, B](ma: F[A])(f: A => F[B]): F[B]

    override def map[A, B](ma: F[A])(f: A => B): F[B] =
      flatMap(ma)(a => unit(f(a)))
  }

  val optionMonad: Monad[Option] = new Monad[Option] {
    override def unit[A](a: => A): Option[A] = Some(a)

    override def flatMap[A, B](ma: Option[A])(f: A => Option[B]): Option[B] =
      ma match {
        case None => None
        case Some(a) => f(a)
      }
  }

  val maybeName: Option[String] = optionMonad.unit("Fred")
  val maybeSurname: Option[String] = optionMonad.unit("Smith")

  val mayBeFullName =
    optionMonad.flatMap(maybeName) { name =>
      optionMonad.map(maybeSurname) { surname =>
        s"$name $surname"
      }
    }

  println(mayBeFullName)
}
