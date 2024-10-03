import { Component } from '@angular/core';
import { EmployeeSearchRequest } from '../EmployeeSearchRequest';
import { Employee } from '../employee';
import { EmployeeService } from '../employee.service';


@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrl: './search.component.css'
})
export class SearchComponent {
  searchRequest: EmployeeSearchRequest = {};
  employees: Employee[] = [];

  constructor(private employeeService: EmployeeService) {}

  searchEmployees() {
    this.employeeService.searchEmployees(this.searchRequest).subscribe(
      (data: Employee[]) => {
        this.employees = data;
      },
      (error) => {
        console.error('Error fetching employees', error);
      }
    );
  }

}
