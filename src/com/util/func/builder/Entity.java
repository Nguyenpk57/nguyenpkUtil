package com.util.func.builder;

import com.util.func.cache.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Entity extends BaseEntity {
    private Long id;

    private String name;

    private String code;

    private String isdn;

    public Entity() {
    }

    protected Entity(EntityBuilder<?, ?> b) {
        this.id = b.id;
        this.name = b.name;
        this.code = b.code;
        this.isdn = b.isdn;
    }

    public static abstract class EntityBuilder<C extends Entity, B extends EntityBuilder<C, B>> {
        protected abstract B self();

        public abstract C build();

        private Long id;

        private String name;

        private String code;

        private String isdn;

        protected EntityBuilder() {
        }

        protected EntityBuilder(Entity c) {
            this.id = c.id;
            this.name = c.name;
            this.code = c.code;
            this.isdn = c.isdn;
        }

        public B id(Long id) {
            this.id = id;
            return self();
        }

        public B name(String name) {
            this.name = name;
            return self();
        }

        public B code(String code) {
            this.code = code;
            return self();
        }

        public B isdn(String isdn) {
            this.isdn = isdn;
            return self();
        }

    }

    public static EntityBuilder<?, ?> builder() {
        return new EntityBuilderImpl();
    }

    public static EntityBuilder<?, ?> builder(Entity c) {
        return new EntityBuilderImpl(c);
    }

    private static final class EntityBuilderImpl extends EntityBuilder<Entity, EntityBuilderImpl> {
        private EntityBuilderImpl() {
        }

        private EntityBuilderImpl(Entity c) {
            super(c);
        }

        @Override
        protected EntityBuilderImpl self() {
            return this;
        }

        @Override
        public Entity build() {
            return new Entity(this);
        }
    }
}
