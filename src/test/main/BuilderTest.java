package main;

import com.util.func.GsonUtils;
import com.util.func.builder.Entity;

public class BuilderTest {
    public static void main(String[] args) throws Exception {
        Entity entity = Entity.builder()
                .id(1L)
                .name("Nguyenpk")
                .code("C6972459")
                .isdn("929394935")
                .build();
        System.out.println("Entity: " + GsonUtils.getInstance().to(entity));
    }
}
