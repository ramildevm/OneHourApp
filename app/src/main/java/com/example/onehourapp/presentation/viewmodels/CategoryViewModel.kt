package com.example.onehourapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.onehourapp.data.CategoryDataRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: CategoryDataRepositoryImpl
): ViewModel() {
    /*
    internal val allCategories: Flow<List<Category>> = repository.getCategories()
    var sortType:SortType = SortType.DEFAULT
    val insertResult = MutableLiveData<Int>()

    fun getCategories():Flow<List<Category>> =
        when(sortType){
            SortType.DEFAULT -> repository.getCategories()
            SortType.COLOR -> repository.getCategories().map { list-> list.sortedWith{ item1, item2->
                SortingHelper.compareColors(item1.color, item2.color)
            } }
            SortType.NAME -> repository.getCategories().map { list-> list.sortedBy { it.name } }
        }

    fun getCategoryById(categoryId: Int) = repository.getCategoryById(categoryId)

    fun insertCategory(category: Category){
        viewModelScope.launch{
            val id = repository.insertCategory(category)
            insertResult.postValue(id.toInt())
        }
    }
    fun deleteCategory(category: Category){
        viewModelScope.launch{
            repository.deleteCategory(category)
        }
    }


    sealed class SortType{
        object DEFAULT:SortType()
        object NAME:SortType()
        object COLOR:SortType()
    }

     */
}
