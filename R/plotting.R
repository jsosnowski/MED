library(arules)

runCharm("agaricus-lepiota.data", 0.4, "output")
runCloset("closet.tdb/mush.tdb", 0.4, "cOut")

supports <- seq(0.1, 0.9, 0.1)
charmSeries <- sapply(supports, function(i) runCharm("agaricus-lepiota.data", i, "output"))
closetSeries <- sapply(supports, function(i) runCloset("closet.tdb/mush.tdb", i, "cOut"))

#pdf("Charm")
#dev.off()

yRange <- range(charmSeries, closetSeries)
plot(supports, charmSeries, ylim = yRange, xlab = "Progi wsparcia", ylab = "Czas wykonania", "b", col = "red")
par(new = T)
plot(supports, closetSeries, ylim = yRange, xlab = "Progi wsparcia", ylab = "Czas wykonania", "b")

pdf("Mushroom")
chS <- sapply(charmSeries[2:9], function(x) x/1000000)
ctS <- sapply(closetSeries[2:9], function(x) x/1000000)
yRange <- range(chS, ctS)
plot(supports[2:9], chS, ylim = yRange, xlab = "Progi wsparcia", ylab = "Czas wykonania (ms)", "b", col = "red")
par(new = T)
plot(supports[2:9], ctS, ylim = yRange, xlab = "Progi wsparcia", ylab = "Czas wykonania (ms)", "b")
rm (chS, ctS)
dev.off()


### CAR ####
convertToTDB("car/car.data", "car/car.tdb")

supports <- seq(0.1, 0.9, 0.1)
car_charmSeries <- sapply(supports, function(i) runCharm("car/car.data", i, "output"))
car_closetSeries <- sapply(supports, function(i) runCloset("car/car.tdb", i, "cOut"))

pdf("Car")
b <- 1
chS <- sapply(car_charmSeries[b:9], function(x) x/1000000)
ctS <- sapply(car_closetSeries[b:9], function(x) x/1000000)
yRange <- range(chS, ctS)
plot(supports[b:9], chS, ylim = yRange, xlab = "Progi wsparcia", ylab = "Czas wykonania (ms)", "b", col = "red")
par(new = T)
plot(supports[b:9], ctS, ylim = yRange, xlab = "Progi wsparcia", ylab = "Czas wykonania (ms)", "b")
rm (chS, ctS, b)
dev.off()



### CONNECT-4 ####
convertToTDB("connect-4/connect-4.data", "connect-4/connect-4.tdb")

supports <- seq(0.4, 0.9, 0.1)
connect_charmSeries <- sapply(supports, function(i) runCharm("connect-4/connect-4.data", i, "output"))
connect_closetSeries <- sapply(supports, function(i) runCloset("connect-4/connect-4.tdb", i, "cOut"))

pdf("Connect-4")
b <- 1
chS <- sapply(connect_charmSeries[b:9], function(x) x/1000000)
ctS <- sapply(connect_charmSeries[b:9], function(x) x/1000000)
yRange <- range(chS, ctS)
plot(supports[b:9], chS, ylim = yRange, xlab = "Progi wsparcia", ylab = "Czas wykonania (ms)", "b", col = "red")
par(new = T)
plot(supports[b:9], ctS, ylim = yRange, xlab = "Progi wsparcia", ylab = "Czas wykonania (ms)", "b")
rm (chS, ctS, b)
dev.off()

### HOUSE ####
convertToTDB("house/house.data", "house/house.tdb")

supports <- seq(0.1, 0.9, 0.1)
house_charmSeries <- sapply(supports, function(i) runCharm("house/house.data", i, "output"))
house_closetSeries <- sapply(supports, function(i) runCloset("house/house.tdb", i, "cOut"))

pdf("House")
b <- 2
chS <- sapply(house_charmSeries[b:9], function(x) x/1000000)
ctS <- sapply(house_closetSeries[b:9], function(x) x/1000000)
yRange <- range(chS, ctS)
plot(supports[b:9], chS, ylim = yRange, xlab = "Progi wsparcia", ylab = "Czas wykonania (ms)", "b", col = "red")
par(new = T)
plot(supports[b:9], ctS, ylim = yRange, xlab = "Progi wsparcia", ylab = "Czas wykonania (ms)", "b")
rm (chS, ctS, b)
dev.off()

### PROMOTER ####
convertToTDB("promoter/promoter.data", "promoter/promoter.tdb")

supports <- seq(0.1, 0.9, 0.1)
promoter_charmSeries <- sapply(supports, function(i) runCharm("promoter/promoter.data", i, "output"))
promoter_closetSeries <- sapply(supports, function(i) runCloset("promoter/promoter.tdb", i, "cOut"))

pdf("Promotor")
b <- 1
chS <- sapply(promoter_charmSeries[b:9], function(x) x/1000000)
ctS <- sapply(promoter_closetSeries[b:9], function(x) x/1000000)
yRange <- range(chS, ctS)
plot(supports[b:9], chS, ylim = yRange, xlab = "Progi wsparcia", ylab = "Czas wykonania (ms)", "b", col = "red")
par(new = T)
plot(supports[b:9], ctS, ylim = yRange, xlab = "Progi wsparcia", ylab = "Czas wykonania (ms)", "b")
rm (chS, ctS, b)
dev.off()



### SPLICE-JXN ####
convertToTDB("splice-jxn/splice-jxn.data", "splice-jxn/splice-jxn.tdb")

supports <- seq(0.1, 0.9, 0.1)
splice_charmSeries <- sapply(supports[c(7,8,9)], function(i) runCharm("splice-jxn/splice-jxn.data", i, "output"))
splice_closetSeries <- sapply(supports, function(i) runCloset("splice-jxn/splice-jxn.tdb", i, "cOut"))

aaa <- runCharm("splice-jxn/splice-jxn.data", 0.6, "output")

pdf("Slice")
b <- 1
chS <- sapply(splice_charmSeries[b:3], function(x) x/1000000)
ctS <- sapply(splice_closetSeries[b:9], function(x) x/1000000)
yRange <- range(chS, ctS)
xRange <- range(supports)
plot(supports[6:9], c(chS,aaa/1000000), xlim = xRange, ylim = yRange, xlab = "Progi wsparcia", ylab = "Czas wykonania (ms)", "b", col = "red")
par(new = T)
plot(supports[b:9], ctS, ylim = yRange, xlab = "Progi wsparcia", ylab = "Czas wykonania (ms)", "b")
rm (chS, ctS, b)
dev.off()

