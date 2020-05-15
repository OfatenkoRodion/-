import java.awt.{Color, Graphics}

import javax.swing.JPanel

class GameMap(resources: List[Resource], animals: List[Animal], deadAnimals: List[Animal]) extends JPanel{

  override def paint(g: Graphics): Unit = {

    //creation empty map 1000x1000
    g.setColor(Color.white)
    g.fillRect(0, 0, 1000, 1000)

    // x coordinates
    g.setColor(Color.lightGray)
    for {
      x <- 1 to 100
    }{
      g.drawLine(x*10, 0, x*10, 1000)
    }

    // y coordinates
    for {
      y <- 1 to 100
    }{
      g.drawLine(0, y*10, 1000, y*10)
    }

    deadAnimals.foreach(v =>{
      g.setColor(Color.lightGray)
      g.fillOval(v.x-15,v.y-15, 45, 45)
      g.setColor(Color.BLACK)
      g.fillOval(v.x,v.y, 15, 15)
    })

    resources.foreach{v =>
      g.setColor(v.color)
      g.fillOval(v.x,v.y, 40, 40)}

    animals.foreach(v =>{
      g.setColor(Color.lightGray)
      g.fillOval(v.x-15,v.y-15, 45, 45)
      g.setColor(v.color)
      g.fillOval(v.x,v.y, 15, 15)
      g.setColor(Color.BLACK)
      g.drawString(v.hp.toString, v.x,v.y)
    })

  }
}