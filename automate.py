#!/usr/bin/python
import subprocess as sp
arg_list=[['java','-jar','sentise.jar','-algo','SL','-tag', '1'],
['java','-jar','sentise.jar','-algo','SL','-tag','2'],['java','-jar','sentise.jar','-algo','SL','-punctuation','1'],
['java','-jar','sentise.jar','-algo','SL','-punctuation','2'],['java','-jar','sentise.jar','-algo','SL','-root','1'],
['java','-jar','sentise.jar','-algo','SL','-root','2'],['java','-jar','sentise.jar','-algo','SL','-features','2'],
['java','-jar','sentise.jar','-algo','SL','-sentiword','2'],['java','-jar','sentise.jar','-algo','SL','-sentiword','4']]
for arg in arg_list:
	sp.call(arg)
	
