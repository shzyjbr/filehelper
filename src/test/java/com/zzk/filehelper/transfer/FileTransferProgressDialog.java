package com.zzk.filehelper.transfer;

public class FileTransferProgressDialog extends JFrame implements ProgressCallback {
    private final JProgressBar progressBar;

    public FileTransferProgressDialog() {
        super("File Transfer Progress");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 100);
        setLocationRelativeTo(null);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        add(progressBar, BorderLayout.CENTER);

        setVisible(true);
    }

    @Override
    public void update(double progress) {
        int percentage = (int) (progress * 100);
        progressBar.setValue(percentage);
        progressBar.setString(percentage + "%");
    }
}