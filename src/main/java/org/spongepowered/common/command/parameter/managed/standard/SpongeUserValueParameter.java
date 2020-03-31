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
package org.spongepowered.common.command.parameter.managed.standard;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.arguments.EntityArgument;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.ArgumentParseException;
import org.spongepowered.api.command.parameter.ArgumentReader;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.user.UserManager;
import org.spongepowered.common.SpongeCommon;
import org.spongepowered.common.command.brigadier.argument.CatalogedArgumentParser;
import org.spongepowered.common.command.brigadier.context.SpongeCommandContextBuilder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SpongeUserValueParameter extends CatalogedArgumentParser<User> {

    private static final ResourceKey RESOURCE_KEY = ResourceKey.sponge("user");
    private final EntityArgument selectorArgumentType = EntityArgument.player();

    @Override
    @NonNull
    public ResourceKey getKey() {
        return SpongeUserValueParameter.RESOURCE_KEY;
    }

    @Override
    public CompletableFuture<Suggestions> listSuggestions(
            final com.mojang.brigadier.context.CommandContext<?> context,
            final SuggestionsBuilder builder) {
        return super.listSuggestions(context, builder);
    }

    @Override
    @NonNull
    public List<String> complete(@NonNull final CommandContext context) {
        return ImmutableList.of();
    }

    @Override
    @NonNull
    public Optional<? extends User> getValue(
            final Parameter.@NonNull Key<? super User> parameterKey,
            final ArgumentReader.@NonNull Mutable reader,
            final CommandContext.@NonNull Builder context)
            throws ArgumentParseException {
        final String peek = reader.peekString();
        if (peek.startsWith("@")) {
            try {
                final ServerPlayer entity =
                        (ServerPlayer) (this.selectorArgumentType.parse((StringReader) reader)
                                .selectOnePlayer(((SpongeCommandContextBuilder) context).getSource()));
                return Optional.of(entity.getUser());
            } catch (final CommandSyntaxException e) {
                throw reader.createException(Text.of(e.getContext()));
            }
        }

        final UserManager userManager = SpongeCommon.getGame().getServer().getUserManager();
        Optional<User> user;
        try {
            final UUID uuid = UUID.fromString(reader.parseString());
            user = userManager.get(uuid);
        } catch (final Exception e) {
            // if no UUID, get the name. We've already advanced the reader at this point.
            user = userManager.get(peek);
        }

        if (user.isPresent()) {
            return user;
        }

        throw reader.createException(Text.of("Could not find user with user name \"" + peek + "\""));
    }

}
