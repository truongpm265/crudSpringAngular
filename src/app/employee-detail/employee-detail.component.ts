import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EmployeeService } from '../employee.service';  // Import your service
import { Employee } from '../employee';

@Component({
  selector: 'app-employee-detail',
  templateUrl: './employee-detail.component.html',
  styleUrls: ['./employee-detail.component.css']  // Fix typo here
})
export class EmployeeDetailComponent implements OnInit {

  employee: Employee | undefined;

  constructor(
    private route: ActivatedRoute,
    private employeeService: EmployeeService  // Inject your service
  ) { }

  ngOnInit() {

    const routeParams = this.route.snapshot.paramMap;
    const employeeIdFromRoute = Number(routeParams.get('id'));
    this.employeeService.getEmployeeById(employeeIdFromRoute).subscribe(employee => {
      this.employee = employee;
    });
  }
}
