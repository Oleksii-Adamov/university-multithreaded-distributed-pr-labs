cmake_minimum_required(VERSION 3.15)
project(FoxAlgoCpp)

set(CMAKE_CXX_STANDARD 17)

add_executable(FoxAlgoCpp main.cpp util.h FoxAlgo.cpp FoxAlgo.h FoxProc.cpp FoxProc.h util.cpp CannonProc.cpp CannonProc.h)
