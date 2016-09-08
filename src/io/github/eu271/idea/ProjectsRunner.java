package io.github.eu271.idea;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intellij.execution.ProgramRunnerUtil;
import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.openapi.application.ApplicationStarterEx;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ex.ProjectManagerEx;

public class ProjectsRunner extends ApplicationStarterEx {
	private static final Logger log = LoggerFactory.getLogger(ProjectsRunner.class);

	public boolean isHeadless() {
		return true;
	}

	public boolean canProcessExternalCommandLine() {
		return true;
	}

	@Override
	public String getCommandName() {
		return "run";
	}

	@Override
	public void processExternalCommandLine(String[] args,
			@Nullable
					String currentDirectory) {
		
		if (args.length < 2) {
			log.error("Impossible to launch configuration, incorrect params.");
			return;
		}

		String configurationName = args[1];

		log.info("#processExternalCommandLine");
		ProjectManagerEx pm = ProjectManagerEx.getInstanceEx();

		for (Project project : pm.getOpenProjects()) {
			log.info("Reading project: {}.", project.getBasePath());
			
			System.out.println(String.format("Loading project %s.", project.getName()));

			RunManager runManager = RunManager.getInstance(project);

			List<RunnerAndConfigurationSettings> configurationSettingsList = runManager.getAllSettings();

			log.info("Runner manager for project {} is {} configuration count {}", project, runManager);

			for (RunnerAndConfigurationSettings runConfiguration : configurationSettingsList) {

				log.info("Run configuration {}", runConfiguration.getName());

				if (configurationName.equals(runConfiguration.getName())) {
					ProgramRunnerUtil.executeConfiguration(project, runConfiguration, DefaultRunExecutor.getRunExecutorInstance());
				}
			}
		}
	}

	@Override
	public void premain(String[] strings) {
	}

	@Override
	public void main(String[] strings) {
		System.exit(0);
	}
}
