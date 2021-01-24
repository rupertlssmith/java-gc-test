filenames <- list.files(getwd(), pattern = "*.csv")

for (i in filenames) {
 file <- gsub(pattern=".csv", replacement="", i)

 latencies <- read.csv(pipe(paste("cat", i, "|grep -v 'Total' | sed 's/Class, Method, Thread, Test Outcome, Time (milliseconds), Memory Used (bytes), Concurrency level, Test Size/Time, Memory, Concurrency, Size/g' | sed 's/^.*Pass, //g' | sed -n '2,12!p'")))
 latencies_cum <- ecdf(latencies$Time)

 png(paste(file,".png",sep=""))
 plot(latencies_cum)
 dev.off()
}