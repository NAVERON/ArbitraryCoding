package com.eron.shipmuticontroller;



public enum ShipStatusEnum {

    DEFAULT(-1, false, "默认", "DEFAULT"), 
    SAIL(0, true, "航行中", "SAIL"), 
    MOORING(1, false, "锚泊", "MOORING"), 
    STRANTED(2, false, "搁浅", "STRANTED"), 
    COLLISION(3, false, "碰撞", "COLLISION") 
    ;
    
    private Integer code;
    private Boolean status;
    private String name;
    private String message;
    
    ShipStatusEnum(Integer code, Boolean status, String name, String message) {
        this.code = code;
        this.status = status;
        this.name = name;
        this.message = message;
    }
    
    public Boolean isMoving() {
        return this.status;
    }
    
    public static Integer getCount() {
        Integer count = -1;
        count += ShipStatusEnum.values().length;
        
        return count;
    }
    
    // 判断是否合法 船舶状态 
    public static Boolean isValidShipStatus(ShipStatusEnum checkStatus) {
        // 如果使用缓存 就不需要这种便利的方式了 hashmap结构比顺序遍历快很多 
        for(ShipStatusEnum status : ShipStatusEnum.values()) {
            if(checkStatus.code == status.code && checkStatus.code != -1) {
                return true;
            }
        }
        
        return false;
    }
    
    
}











