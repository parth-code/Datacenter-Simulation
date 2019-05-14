#Course Project
## Vineet Patel, Parth Mehul Desai, Heng Li


###Dockerfile
You can pull the image from `https://cloud.docker.com/u/vineet1999/repository/docker/vineet1999/cs441_group_project`
Do build the image run `docker build -t <image name you would like> .`
Make sure when you build, your current directory is the root directory
of this project, as the files will be copied over when building the image. 

To run run the container run
`docker run -it <image name>`
Note it is important that you include the -it flag, as user input is need
to select which simulation you wish to run. 

NOTE: This may take a while to build and run, so please have patience


###Running
Run the files using `sbt clean compile run`.
Tests can be run using `sbt test`.

###Report
The report is in ProjectReport.pdf. It outlines the simulation, and showcases the results.
