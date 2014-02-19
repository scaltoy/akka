package akka.streams.io

import akka.actor.ActorSystem
import java.net.InetSocketAddress
import akka.util.ByteString
import akka.streams.{ ActorBasedImplementationSettings, Operation, ActorBasedImplementationFactory }

object TcpEchoServer {
  import Operation._

  def main(args: Array[String]) {
    implicit val system = ActorSystem("tcpdemo")
    implicit val factory = new ActorBasedImplementationFactory(ActorBasedImplementationSettings(system))
    TcpStream.listen(new InetSocketAddress("localhost", 1111)).foreach {
      case (address, (in, out)) ⇒
        println(s"Client connected: $address")
        in.map(ByteString("Hello ") ++ _).finish(out).run()

      //in.fold(0)(_ + _.map(_.toInt).sum).map(i ⇒ ByteString(i.toString)).finish(FromConsumerSink(out)).run()
    }.run()
    /*// alternative
    TcpStream.listenAndHandle(new InetSocketAddress("localhost", 1111)) { peer ⇒
      identity
    }*/

    println("Echo server started, type RETURN to exit.")
    //(Std.Console.until(_ == "exit") onComplete system.shutdown()).foreach(println) // Poor mans echo...
    Console.readLine()
    system.shutdown()
  }
}