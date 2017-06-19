package org.cae.entity;

import static org.cae.common.Util.toJson;;

public class Entity {

	public String toString(){
		return toJson(this);
	}
}
