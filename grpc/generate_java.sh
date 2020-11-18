#!/bin/zsh
# Make sure to install 'protoc' using 'brew install protobuf'
print "SimDeviceBLE gRPC code generator"
print "Cleaning up generated code..."
rm -rf ./generated_java
mkdir generated_java

print "Running code generator..."
protoc -I=./ --java_out=./generated_java ./SimDeviceBLE.proto
print "Code generation completed!"