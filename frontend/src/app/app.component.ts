import { Component } from '@angular/core';

/**
 * Root application component.
 * Hosts the top-level UI (router outlet) and global shell.
 * The `title` property is used by templates for simple branding/demo text.
 */
@Component({
	selector: 'app-root',
	templateUrl: './app.component.html'
})

export class AppComponent {
	// Simple title shown in the header; safe to change for branding
	public title = 'angularDemo';
}
