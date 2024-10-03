import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom, Observable } from 'rxjs';
import { Employee } from './employee';
import { EmployeeSearchRequest } from './EmployeeSearchRequest';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  private baseUrl="http://localhost:8080/api/employee"

  constructor(private httpClient:HttpClient) { }

  getEmployeeList():Observable<Employee[]>{
    return this.httpClient.get<Employee[]>(`${this.baseUrl + "/"}`);
  }
  //-- using Promise, using firstValueFrom from rxjs for return
  getEmployeeListPromise(): Promise<Employee[]> {
    return firstValueFrom(this.httpClient.get<Employee[]>(`${this.baseUrl}/`));
  }

  createEmployee(employee:Employee):Observable<object>{
    return this.httpClient.post(`${this.baseUrl + "/"}`,employee)
  }

  getEmployeeById(id:number):Observable<Employee>{
    return this.httpClient.get<Employee>(`${this.baseUrl }/${id}`);
  }

  updateEmployee(id:number,employee:Employee):Observable<Employee>{
    return this.httpClient.put<Employee>(`${this.baseUrl}/${id}`,employee);
  }
  
  deleteEmployee(id:number):Observable<object>{
    return this.httpClient.delete<Employee>(`${this.baseUrl}/${id}`);
  }
  // searchEmployeeByName(name: string): Observable<Employee[]> {
  //   const body = { name: name };
  //   return this.httpClient.post<Employee[]>(`${this.baseUrl}/searchJson`, body);
  // }
  searchEmployees(request: EmployeeSearchRequest): Observable<Employee[]> {
    return this.httpClient.post<Employee[]>(`${this.baseUrl}/searchJson`, request);
  }
}
