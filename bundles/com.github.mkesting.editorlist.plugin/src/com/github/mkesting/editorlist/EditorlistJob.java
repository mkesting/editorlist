package com.github.mkesting.editorlist;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.progress.UIJob;

public class EditorlistJob extends UIJob {

    private final Queue<Runnable> updateJobs;
    private final EditorlistView editorlistView;

    public EditorlistJob(final EditorlistView editorlistView) {
        super("Editorlist Job");
        setSystem(true);

        this.editorlistView = editorlistView;
        this.updateJobs = new ConcurrentLinkedQueue<>();
    }

    public void selectEditorInViewer(final IEditorPart editor) {
        updateJobs.add(() -> editorlistView.selectEditorInViewer(editor) );
        schedule();
    }

    public void addEditorToViewer(final IEditorPart editor) {
        updateJobs.add(() -> editorlistView.addEditorToViewer(editor));
        schedule();
    }

    public void removeEditorFromViewer(final IEditorPart editor) {
        updateJobs.add(() -> editorlistView.removeEditorFromViewer(editor));
        schedule();
    }

    public void refreshLabel(final IEditorPart editor) {
        updateJobs.add(() -> editorlistView.refreshLabel(editor));
    }

    public void refreshContents() {
        updateJobs.add(editorlistView::refreshContents);
        schedule();
    }

    public void activateSelectedEditor() {
        updateJobs.add(editorlistView::activateSelectedEditor);
        schedule();
    }

    @Override
    public IStatus runInUIThread(IProgressMonitor monitor) {
        Runnable currentJob = null;
        while ((currentJob = updateJobs.poll()) != null) {
            currentJob.run();
        }
        return Status.OK_STATUS;
    }
}
