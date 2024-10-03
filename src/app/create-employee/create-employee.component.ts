import { Component } from '@angular/core';
import { Employee } from '../employee';
import { EmployeeService } from '../employee.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-employee',
  templateUrl: './create-employee.component.html',
  styleUrls: ['./create-employee.component.css']
})
export class CreateEmployeeComponent {

  employee: Employee = new Employee();
  isFormSubmitted: boolean = false;

  constructor(private employeeService: EmployeeService, private router: Router) {}

  // Submit method
  onSubmit(employeeForm: any) {
    this.isFormSubmitted = true;

    // Only insert new employee if the form is valid
    if (employeeForm.valid) {
      this.insertNewEmployee();
      console.log(this.employee);
    } else {
      console.log('Form is invalid');
    }
  }

  // Insert new employee to the backend
  insertNewEmployee() {
    this.employeeService.createEmployee(this.employee).subscribe(
      (data) => {
        this.goToEmployeeList();
        console.log(data);
      },
      (error) => {
        console.error('Error creating employee:', error);
      }
    );
  }

  goToEmployeeList() {
    this.router.navigate(['/employees']);
  }
}
