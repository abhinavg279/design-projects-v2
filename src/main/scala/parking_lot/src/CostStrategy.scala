package parking_lot.src

import org.joda.time.DateTime
import parking_lot.src.model.{Bike, Car, VehicleType}

sealed trait CostStrategy {
  def calculatePrice(startTime: DateTime, endTime: DateTime, vehicleType: VehicleType): BigDecimal
}

object CostStrategy {
  case object FixedPriceStrategy extends CostStrategy {
    override def calculatePrice(startTime: DateTime, endTime: DateTime, vehicleType: VehicleType): BigDecimal = {
      val minutesOfUse: Long = (endTime.getMillis - startTime.getMillis) / (1000 * 60)
      vehicleType match {
        case Car => 12.5 * minutesOfUse
        case Bike => 10 * minutesOfUse
      }
    }
  }
}
