package com.excsi.mixins;

import io.github.tox1cozz.mixinbooterlegacy.ILateMixinLoader;
import io.github.tox1cozz.mixinbooterlegacy.LateMixin;

import java.util.Collections;
import java.util.List;
@LateMixin
public class LateMixinLoader implements ILateMixinLoader {
    @Override
    public List<String> getMixinConfigs() {
        return Collections.singletonList("mixin.goldenswords.json");
    }
}