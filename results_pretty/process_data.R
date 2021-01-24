filenames <- list.files(getwd(), pattern = "*.csv")
#colours <- c("blue", "red", "green", "orange", "cyan", "magenta")
colours <- c("dark blue", "blue", "light blue", "cyan", "red")

png("graph.png", width=800, height=600, units="px")
op <- par(bg="white")  
plot.new()


j=1
for (i in filenames) {
 file <- gsub(pattern=".csv", replacement="", i)

 latencies <- read.csv(pipe(paste("cat", i, "|grep -v 'Total' | sed 's/Class, Method, Thread, Test Outcome, Time (milliseconds), Memory Used (bytes), Concurrency level, Test Size/Time, Memory, Concurrency, Size/g' | sed 's/^.*Pass, //g' | sed -n '2,12!p'")))
 latencies_cum <- ecdf(latencies$Time)
 
 if (j==1)
 {
  plot(latencies_cum, do.points=FALSE, verticals=TRUE, lty='solid', lwd=4, col=colours[j], xlab="latency / ms", ylab="fraction", main="Latency Comparison of Javolution Object Pooling vs Garbage Collection", ylim=c(0, 1))
  grid(NULL, NULL, col="black", lty="dotted")           
 }
 else
 {
  plot(latencies_cum, do.points=FALSE, verticals=TRUE, lty='solid', lwd=4, col=colours[j], add=TRUE)
 }
 
 filenames[j]=file
 j=j+1
}

legend(x="right", inset=0.04, filenames, cex=1.5, col=colours, pch=16:16, ncol=1)
par(op)

dev.off()