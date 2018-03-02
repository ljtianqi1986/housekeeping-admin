package com.framework.utils;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class UuidUtil {

	public static String get32UUID() {
		String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
		return uuid;
	}

	public static String getRndNumCode()
	{
		Random rand = new Random();
		Date now = new Date();
		long t = now.getTime();
		return (Long.valueOf(t * 1000L + (long)rand.nextInt(1000))).toString();
	}
	
	public static void main(String[] args) {
		System.out.println(get32UUID());
	}
}

