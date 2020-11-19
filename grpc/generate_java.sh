#!/bin/zsh
# Make sure to install 'protoc' using 'brew install protobuf'
# Helpful article: https://www.baeldung.com/grpc-introduction#1-using-protocol-buffer-compiler
print "SimDeviceBLE gRPC code generator"
print "Cleaning up generated code..."
rm -rf ./generated_java
mkdir generated_java

print "Running code generator..."
protoc --plugin=protoc-gen-grpc-java=compiler/protoc-gen-grpc-java -I=./ --java_out=./generated_java --grpc-java_out=./generated_java ./SimDeviceBLE.proto
print "Code generation completed!"

print "Applying generated code to project source"
rm -rf ../app/build/generated/src
mkdir -p ../app/build/generated/src/nl/rwslinkman/simdeviceble/grpc/server/
cp generated_java/nl/rwslinkman/simdeviceble/grpc/server/* ../app/build/generated/src/nl/rwslinkman/simdeviceble/grpc/server/
print "Copied files to 'app/build/generated/src' directory!"