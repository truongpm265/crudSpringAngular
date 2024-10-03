import { Component, Input } from '@angular/core';
import { Employee } from '../employee';
import { EmployeeService } from '../employee.service';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { EmployeeSearchRequest } from '../EmployeeSearchRequest';

@Component({
  selector: 'app-list-employee',
  templateUrl: './list-employee.component.html',
  styleUrl: './list-employee.component.css'
})
export class ListEmployeeComponent {

  searchRequest: EmployeeSearchRequest = {};
  @Input() employees: Employee[] = [];
    searchName: string = '';
    constructor(private employeeService: EmployeeService, private route:Router){
    }

    ngOnInit():void{
      this.loadEmployees();
    }

    getEmployees(){
      this.employeeService.getEmployeeList().subscribe(data =>{
        this.employees=data;
      })
    }
    //---------- using async/await to get employee list--- 
    async loadEmployees() {
      try {
        this.employees = await firstValueFrom(this.employeeService.getEmployeeList()); //firstValueFrom from rxjs thay cho .subscribe
      } catch (error) {
        console.error('Error fetching employees:', error);
      }
    }
  //------------------------
    updateEmployee(id:number){
      this.route.navigate(['update-employee',id])
    }
    deleteEmployee(id: number) {
      const confirmed = window.confirm('Bạn có chắc chắn muốn xóa nhân viên này không?');
    
      if (confirmed) {
        this.employeeService.deleteEmployee(id).subscribe({
          next: (data) => {
            console.log('Delete successful:', data);
            this.getEmployees(); // Cập nhật danh sách sau khi xóa thành công
          },
          error: (err) => {
            console.error('Error deleting employee:', err); // Log lỗi ra console nếu xảy ra
          }
        });
      }
    }

    // searchEmployeesByName() {
    //   if (this.searchName.trim()) {
    //     this.employeeService.searchEmployeeByName(this.searchName).subscribe(data => {
    //       this.employees = data;
    //     });
    //   } else {
    //     this.getEmployees();
    //   }
    // }
    searchEmployees() {
      this.employeeService.searchEmployees(this.searchRequest).subscribe(
        (data: Employee[]) => {
          this.employees = data;
        },
        (error) => {
          this.getEmployees();
        }
      );
    }
    
    
}
