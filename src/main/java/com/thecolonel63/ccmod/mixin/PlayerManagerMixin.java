package com.thecolonel63.ccmod.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
    @WrapOperation(method = "checkCanJoin", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;isWhitelisted(Lcom/mojang/authlib/GameProfile;)Z"))
    private boolean wrapWhitelisted(PlayerManager instance, GameProfile profile, Operation<Boolean> original, @Share("whitelisted") LocalBooleanRef whitelisted) {
        whitelisted.set(original.call(instance, profile));
        return true;
    }

    @WrapMethod(method = "checkCanJoin")
    private Text addDiscordCheck(SocketAddress address, GameProfile profile, Operation<Text> original, @Share("whitelisted") LocalBooleanRef whitelisted) {
        Text value = original.call(address, profile);
        if (value != null) return value;
        if (whitelisted.get()) return null;
        return discordText(profile);
    }

    @Unique
    @Nullable
    private Text discordText(GameProfile profile) {
        try (Socket socket = new Socket("e54371df-71b4-4165-bf75-c7e9ede05f09", 25687)) {
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            sendRequest(profile.getId(), profile.getName(), out);
            String value = getResponse(in);
            return value.isEmpty() ? null : Text.of(value);
        } catch (Exception e) {
            return Text.of("Failed to verify with Discord. Please try again later!");
        }
    }

    @Unique
    private void sendRequest(UUID id, String name, OutputStream out) throws IOException {
        String uuid = id.toString();
        ByteBuffer outBuf = ByteBuffer.allocate(9 + uuid.length() + name.length());
        outBuf.put((byte) 0);
        outBuf.putInt(uuid.length());
        outBuf.put(uuid.getBytes(StandardCharsets.UTF_8));
        outBuf.putInt(name.length());
        outBuf.put(name.getBytes(StandardCharsets.UTF_8));
        out.write(fromInt(outBuf.position()));
        out.write(outBuf.array(), 0, outBuf.position());
    }

    @Unique
    private String getResponse(InputStream in) throws IOException {
        ByteBuffer lengthBuf = ByteBuffer.wrap(in.readNBytes(4));
        ByteBuffer inBuf = ByteBuffer.wrap(in.readNBytes(lengthBuf.getInt()));

        if (inBuf.get() != 0) throw new RuntimeException();
        byte[] stringBytes = new byte[inBuf.getInt()];
        inBuf.get(stringBytes);
        return new String(stringBytes);
    }

    @Unique
    private byte[] fromInt(int value) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(value);
        return buffer.array();
    }

}
