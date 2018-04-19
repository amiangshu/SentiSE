#!/usr/bin/python
import subprocess as sp
arg_list=[['java','-jar','sentise.jar','-algo','SVM'],
['java','-jar','sentise.jar','-algo','SL'],['java','-jar','sentise.jar','-algo','KNN'],['java','-jar','sentise.jar','-algo','RC']]
for arg in arg_list:
	sp.call(arg)
	
