import { Component } from "@angular/core";

/**
 * Simple landing/home component. Keeps the home view isolated so it can
 * be extended with content or route-specific logic in the future.
 */
@Component({
	selector: "app-homepage",
	templateUrl: "./home.component.html", 
})

export class HomeComponent {
}