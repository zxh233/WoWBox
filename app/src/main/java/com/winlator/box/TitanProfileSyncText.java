package com.winlator.box;

import android.content.Context;

import com.winlator.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TitanProfileSyncText {
    private static final Pattern LAST_SYNC = Pattern.compile(
            "Last sync: ([^\\n]*)\\nWTF: ([\\d,]+) files\\nAddOns: ([\\d,]+) files\\nBytes: ([\\d,]+)\\nSnapshot: ([^\\n]*)");
    private static final Pattern CONNECTION_READY = Pattern.compile(
            "Connection ready: ([\\d,]+) WTF files, ([\\d,]+) AddOns files, ([\\d,]+) bytes");
    private static final Pattern SYNC_COMPLETE = Pattern.compile(
            "Sync complete: ([\\d,]+) WTF files and ([\\d,]+) AddOns files");
    private static final Pattern RECEIVING_PROGRESS = Pattern.compile(
            "Receiving: ([\\d,]+) / ([\\d,]+) files, ([\\d,]+) / ([\\d,]+) bytes");

    private TitanProfileSyncText() {}

    public static String localize(Context context, String message) {
        if (message == null || message.isEmpty()) return "";
        String exact = localizeExact(context, message);
        if (exact != null) return exact;

        Matcher matcher = LAST_SYNC.matcher(message);
        if (matcher.matches()) {
            return context.getString(R.string.titan_last_sync,
                    matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5));
        }
        matcher = CONNECTION_READY.matcher(message);
        if (matcher.matches()) {
            return context.getString(R.string.titan_connection_ready,
                    matcher.group(1), matcher.group(2), matcher.group(3));
        }
        matcher = SYNC_COMPLETE.matcher(message);
        if (matcher.matches()) {
            return context.getString(R.string.titan_sync_complete, matcher.group(1), matcher.group(2));
        }
        matcher = RECEIVING_PROGRESS.matcher(message);
        if (matcher.matches()) {
            return context.getString(R.string.titan_receiving_progress,
                    matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4));
        }

        String detailed = localizeWithDetail(context, message, "Remote preflight failed",
                R.string.titan_remote_preflight_failed, R.string.titan_remote_preflight_failed_detail);
        if (detailed != null) return detailed;
        detailed = localizeWithDetail(context, message, "Remote tar failed",
                R.string.titan_remote_tar_failed, R.string.titan_remote_tar_failed_detail);
        if (detailed != null) return detailed;
        detailed = localizeWithDetail(context, message, "SSH connection failed",
                R.string.titan_ssh_failed, R.string.titan_ssh_failed);
        if (detailed != null) return detailed;
        if (message.startsWith("Trusted ")) {
            return context.getString(R.string.titan_trusted_fingerprint, message.substring("Trusted ".length()));
        }
        return message;
    }

    private static String localizeWithDetail(Context context, String message, String prefix, int bareResId, int detailResId) {
        if (message.equals(prefix)) return context.getString(bareResId);
        String separator = prefix + ": ";
        if (!message.startsWith(separator)) return null;
        String detail = message.substring(separator.length());
        String known = localizeExact(context, detail);
        return context.getString(detailResId, known != null ? known : detail);
    }

    private static String localizeExact(Context context, String message) {
        switch (message) {
            case "Ready": return context.getString(R.string.titan_status_ready);
            case "No successful sync recorded": return context.getString(R.string.titan_no_sync_recorded);
            case "Last sync record is unreadable": return context.getString(R.string.titan_last_sync_unreadable);
            case "Host key was not trusted": return context.getString(R.string.titan_host_not_trusted);
            case "Trusted SSH host key removed": return context.getString(R.string.titan_trusted_removed);
            case "Sync cancelled": return context.getString(R.string.titan_sync_cancelled);
            case "Reading SSH host key…": return context.getString(R.string.titan_reading_host_key);
            case "Confirm SSH host key": return context.getString(R.string.titan_confirm_host_key);
            case "Connecting to SSH host…": return context.getString(R.string.titan_connecting);
            case "SSH password is required": return context.getString(R.string.titan_password_required);
            case "Checking the Windows source…": return context.getString(R.string.titan_checking_source);
            case "Receiving WTF and AddOns…": return context.getString(R.string.titan_receiving);
            case "Checking that the Windows source stayed unchanged…": return context.getString(R.string.titan_checking_unchanged);
            case "The Windows source changed during sync; nothing was installed": return context.getString(R.string.titan_source_changed);
            case "Activating the verified Titan profile…": return context.getString(R.string.titan_activating);
            case "Box is not installed": return context.getString(R.string.titan_box_not_installed);
            case "Stop every Windows program before syncing": return context.getString(R.string.titan_stop_windows);
            case "China Titan is not installed in the standard Box": return context.getString(R.string.titan_titan_not_installed);
            case "Remote profile exceeds the 250,000-file limit": return context.getString(R.string.titan_file_limit);
            case "Remote profile exceeds the 16 GiB limit": return context.getString(R.string.titan_size_limit);
            case "Unable to determine free storage": return context.getString(R.string.titan_storage_unknown);
            case "Not enough free space for safe staging": return context.getString(R.string.titan_not_enough_space);
            case "Received files do not match the Windows snapshot": return context.getString(R.string.titan_snapshot_mismatch);
            case "SSH host is required": return context.getString(R.string.titan_host_required);
            case "SSH host is invalid": return context.getString(R.string.titan_host_invalid);
            case "SSH port must be between 1 and 65535": return context.getString(R.string.titan_port_invalid);
            case "SSH username is required": return context.getString(R.string.titan_username_required);
            case "SSH username is invalid": return context.getString(R.string.titan_username_invalid);
            case "Source path must be an absolute Windows path without shell characters": return context.getString(R.string.titan_source_path_invalid);
            case "Unable to obtain the SSH host key": return context.getString(R.string.titan_host_key_unavailable);
            case "Remote preflight returned invalid metadata": return context.getString(R.string.titan_remote_metadata_invalid);
            case "SSH host key is not trusted": return context.getString(R.string.titan_host_key_untrusted);
            case "SSH host key changed; forget it and trust the new key": return context.getString(R.string.titan_host_key_changed);
            case "SSH command timed out": return context.getString(R.string.titan_ssh_timeout);
            case "SSH command interrupted": return context.getString(R.string.titan_ssh_interrupted);
            case "Windows source WoW is running": return context.getString(R.string.titan_wow_running);
            case "Windows source WTF directory is missing": return context.getString(R.string.titan_wtf_missing);
            case "Windows source AddOns directory is missing": return context.getString(R.string.titan_addons_missing);
            case "Windows tar.exe is unavailable": return context.getString(R.string.titan_tar_missing);
            case "Windows source contains a reparse point": return context.getString(R.string.titan_reparse_point);
            default: return null;
        }
    }
}
