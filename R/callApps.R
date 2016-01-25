runCharm <- function(datasetName, support, outputName) 
{
  command=paste("java", "-jar", "source-all-1.0.jar", datasetName, support, outputName)
  system(command, ignore.stdout = T, ignore.stderr = T, intern = F)
  return(read.table(outputName))
}

runCloset <- function(datasetName, support, outputName) 
{
  command=paste("java", "-jar", "closet.jar", datasetName, support, ">", outputName)
  system(command, ignore.stdout = F, ignore.stderr = T, intern = F)
  return(read.table(outputName))
}

convertToTDB <- function(dataset, output)
{
  dsOrg <- read.table(dataset, sep=",")
  dsTDB <- as(dsOrg, "transactions")
  write (dsTDB, output, sep = ",", quote = F)
}