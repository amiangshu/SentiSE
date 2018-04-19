#!/usr/bin/python
import subprocess as sp
arg_list=[['java','-jar','sentise.jar','-algo','SVM','-punctuation','1'],
['java','-jar','sentise.jar','-algo','SVM','-punctuation','2'],['java','-jar','sentise.jar','-algo','SVM','-root','1'],
['java','-jar','sentise.jar','-algo','SVM','-root','2'],['java','-jar','sentise.jar','-algo','SVM','-features','2'],
['java','-jar','sentise.jar','-algo','SVM','-sentiword','2'],['java','-jar','sentise.jar','-algo','SVM','-sentiword','4']]
for arg in arg_list:
	sp.call(arg)
	
