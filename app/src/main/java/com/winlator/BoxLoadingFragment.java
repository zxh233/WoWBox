package com.winlator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.winlator.box.BoxDebugServer;
import com.winlator.box.BoxInstallListener;
import com.winlator.box.BoxInstaller;
import com.winlator.box.BoxRuntime;

public class BoxLoadingFragment extends Fragment implements BoxInstallListener {
    private ProgressBar overallProgressBar;
    private ProgressBar currentProgressBar;
    private TextView stageView;
    private TextView detailView;
    private TextView statusView;
    private Button retryButton;
    private Button dryRunButton;
    private volatile boolean started;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.box_loading_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        overallProgressBar = view.findViewById(R.id.OverallProgressBar);
        currentProgressBar = view.findViewById(R.id.CurrentProgressBar);
        stageView = view.findViewById(R.id.TVStage);
        detailView = view.findViewById(R.id.TVDetail);
        statusView = view.findViewById(R.id.TVStatus);
        retryButton = view.findViewById(R.id.BTRetry);
        dryRunButton = view.findViewById(R.id.BTDryRun);

        retryButton.setOnClickListener((v) -> startInstall(false));
        dryRunButton.setOnClickListener((v) -> startInstall(true));

        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(R.string.box_loading_title);
        if (BoxRuntime.get(requireContext()).isInstalled()) {
            ((MainActivity) requireActivity()).showLauncher();
            return;
        }
        startInstall(false);
    }

    private void startInstall(boolean dryRun) {
        if (started) return;
        started = true;
        retryButton.setEnabled(false);
        dryRunButton.setEnabled(false);
        statusView.setText(dryRun ? R.string.box_dry_run_running : R.string.box_install_running);
        new BoxInstaller(requireContext()).installAsync(dryRun, (success) -> requireActivity().runOnUiThread(() -> {
            started = false;
            retryButton.setEnabled(true);
            dryRunButton.setEnabled(true);
            if (success) {
                statusView.setText(dryRun ? R.string.box_dry_run_complete : R.string.box_install_complete);
                if (!dryRun) {
                    BoxDebugServer.get(requireContext()).start();
                    ((MainActivity) requireActivity()).showLauncher();
                }
            } else {
                statusView.setText(R.string.box_install_failed);
            }
        }), this);
    }

    private String localizeStage(String stage) {
        if (stage == null) return "";
        switch (stage) {
            case "prepare": return getString(R.string.box_stage_prepare);
            case "release-prepare": return getString(R.string.box_stage_release_prepare);
            case "release-activate": return getString(R.string.box_stage_release_activate);
            case "release-complete": return getString(R.string.box_stage_release_complete);
            case "already-installed": return getString(R.string.box_stage_already_installed);
            case "prefix-template": return getString(R.string.box_stage_prefix_template);
            case "payload": return getString(R.string.box_stage_payload);
            case "base-imagefs": return getString(R.string.box_stage_base_imagefs);
            case "wine-runtime": return getString(R.string.box_stage_wine_runtime);
            case "graphics-wrapper-stack": return getString(R.string.box_stage_graphics_wrapper);
            case "input-bridge-stack": return getString(R.string.box_stage_input_bridge);
            case "dx-wrapper-stack": return getString(R.string.box_stage_dx_wrapper);
            case "gpu-driver-stack": return getString(R.string.box_stage_gpu_driver);
            case "cpu-emu-stack": return getString(R.string.box_stage_cpu_emu);
            case "payload-directory": return getString(R.string.box_stage_payload_directory);
            case "payload-download": return getString(R.string.box_stage_payload_download);
            case "payload-extract": return getString(R.string.box_stage_payload_extract);
            case "frame-generation-stack": return getString(R.string.box_stage_frame_generation);
            case "dry-run-complete": return getString(R.string.box_stage_dry_run_complete);
            case "complete": return getString(R.string.box_stage_complete);
            default: return stage;
        }
    }

    private String localizeDetail(String detail) {
        if (detail == null) return "";
        switch (detail) {
            case "Preparing box install": return getString(R.string.box_detail_preparing);
            case "Verifying Wine and FEX assets": return getString(R.string.box_detail_verifying_runtime);
            case "Updating verified runtime and prefix": return getString(R.string.box_detail_updating_runtime);
            case "Runtime update complete": return getString(R.string.box_detail_runtime_update_complete);
            case "recorded post-wineboot prefix identity": return getString(R.string.box_detail_recorded_prefix);
            case "preserved existing imagefs": return getString(R.string.box_detail_preserved_imagefs);
            case "imagefs ready": return getString(R.string.box_detail_imagefs_ready);
            case "preserved existing prefix": return getString(R.string.box_detail_preserved_prefix);
            case "preserved existing runtime": return getString(R.string.box_detail_preserved_runtime);
            default: return detail;
        }
    }

    @Override
    public void onProgress(String stage, int overallProgress, int currentProgress, String detail) {
        if (!isAdded()) return;
        requireActivity().runOnUiThread(() -> {
            if (!isAdded()) return;
            overallProgressBar.setProgress(overallProgress);
            currentProgressBar.setProgress(currentProgress);
            stageView.setText(localizeStage(stage));
            detailView.setText(localizeDetail(detail));
        });
    }
}
