/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.common.mixin.api.mcp.particles;

import com.google.common.collect.ImmutableMap;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.effect.particle.ParticleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.common.effect.particle.ParticleOptionDefaults;

import java.util.Map;
import java.util.Optional;

@Mixin(net.minecraft.particles.ParticleType.class)
public abstract class ParticleTypeMixin_API implements org.spongepowered.api.effect.particle.ParticleType {

    private ResourceKey impl$key = null;
    private ImmutableMap<ParticleOption<?>, Object> impl$defaultOptions = null;

    @SuppressWarnings("unchecked")
    @Override
    public <V> Optional<V> getDefaultOption(ParticleOption<V> option) {
        if (this.impl$defaultOptions == null) {
            this.impl$defaultOptions = ParticleOptionDefaults.generateDefaultsForNamed((ParticleType<?>) (Object) this);
        }
        return Optional.ofNullable((V) this.impl$defaultOptions.get(option));
    }

    @Override
    public Map<ParticleOption<?>, Object> getDefaultOptions() {
        if (this.impl$defaultOptions == null) {
            this.impl$defaultOptions = ParticleOptionDefaults.generateDefaultsForNamed((ParticleType<?>) (Object) this);
        }
        return this.impl$defaultOptions;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public ResourceKey getKey() {
        if (this.impl$key == null) {
            final ResourceLocation location = Registry.PARTICLE_TYPE.getKey((net.minecraft.particles.ParticleType<?>) (Object) this);
            this.impl$key = (ResourceKey) (Object) location;
        }
        return this.impl$key;
    }
}